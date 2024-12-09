package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.dto.member.*;
import com.example.shop.dto.instagram.article.ArticleSummaryResponseDto;
import com.example.shop.repository.member.MemberRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ValidationService validationService;
    private final ImageService imageService;

    /** 회원 가입 */
    @Transactional
    public void saveMember(MemberSignUpDto dto) {
        Address address = Address.createAddress(dto.getPostCode(), dto.getRoadAddress(), dto.getRoadAddress());

        Member member = Member.createMember(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getNickname(),
                dto.getEmail(), dto.getPhone(), address);

        memberRepository.save(member);
    }

    /** 회원 정보 수정 */
    @Transactional
    public void updateMemberInfo(Long memberId, MemberUpdateDto dto) {
        Member member = validationService.validateMemberById(memberId);

        Address newAddress = Address.createAddress(dto.getPostCode(), dto.getRoadAddress(), dto.getRoadAddress());
        member.updateMemberInfo(dto.getName(), dto.getNickname(), dto.getEmail(), dto.getPhone(), newAddress);
    }

    /** 회원 계정 상태 수정 (공개/비공개) */
    @Transactional
    public void updateMemberAccountStatus(Long memberId) {
        Member member = validationService.validateMemberById(memberId);

        if (member.isAccountStatus()) {
            member.updateMemberAccountStatus(AccountStatus.PRIVATE);
        } else {
            member.updateMemberAccountStatus(AccountStatus.PUBLIC);
        }
    }

    /** 회원 프로필 이미지 수정 */
    @Transactional
    public void updateMemberProfileImg(Long memberId, MemberProfileUpdateDto dto, MultipartFile file) {
        Member member = validationService.validateMemberById(memberId);

        MemberProfileImg memberProfileImg = imageService.updateMemberProfileImg(member, file);
        member.updateMemberProfile(memberProfileImg, dto.getIntroduction());
    }

    /** 회원의 게시물 조회 (프로필 비공개 시 팔로우 해야만 조회 가능) */
    @Transactional(readOnly = true)
    public Page<ArticleSummaryResponseDto> getArticle(Long memberId, Long targetId, Pageable pageable) {

        Page<Article> articles = memberRepository.findArticleByMemberId(memberId, targetId, pageable);

        List<ArticleSummaryResponseDto> dtos = articles.stream()
                .map(article -> {
                    Long likeId = validationService.findArticleLikeIdByArticleAndMember(article.getId(), memberId);

                    return ArticleSummaryResponseDto.createDto(article.getId(), memberId, article.getMember().getNickname(),
                            article.getArticleImages().get(0).getImgUrl(),
                            article.getContent(), article.getLikes(), article.getViewCounts(), likeId);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, articles.getTotalElements());
    }

    /** 회원의 저장된 게시물 조회 */
    @Transactional(readOnly = true)
    public Page<ArticleCollectionResponseDto> getArticleCollection(Long memberId, Pageable pageable) {

        Page<ArticleCollection> articleCollections = memberRepository.findArticleCollectionByMemberId(memberId, pageable);

        List<ArticleCollectionResponseDto> dtos = articleCollections.stream()
                .map(articleCollection -> {
                    Long likeId = validationService.findArticleLikeIdByArticleAndMember(articleCollection.getArticle().getId(), memberId);

                    return ArticleCollectionResponseDto.createDto(articleCollection.getId(),
                            articleCollection.getArticle().getId(), articleCollection.getMember().getId(),
                            articleCollection.getMember().getNickname(), articleCollection.getArticle().getArticleImages().get(0).getImgUrl(),
                            articleCollection.getArticle().getContent(), articleCollection.getArticle().getLikes(),
                            articleCollection.getArticle().getViewCounts(), likeId);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, articleCollections.getTotalElements());
    }

    /** 회원의 팔로우 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<FollowerListResponseDto> getFollower(Long memberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowerByMemberId(memberId, pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> FollowerListResponseDto.createDto(follower.getId(),
                        follower.getFollower().getId(), follower.getFollower().getNickname()))
                .toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }

    /** 회원의 팔로워 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<FollowerListResponseDto> getFollowee(Long memberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowingByMemberId(memberId, pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> FollowerListResponseDto.createDto(follower.getId(),
                        follower.getFollowee().getId(), follower.getFollowee().getNickname()))
                .toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }

    /** 팔로우를 신청받은 회원의 팔로우 요청 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<FollowerListResponseDto> getFolloweeRequest(Long memberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowingRequestByMemberId(memberId, pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> FollowerListResponseDto.createDto(follower.getId(),
                        follower.getFollowee().getId(), follower.getFollowee().getNickname()))
                .toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }

    /** 회원의 차단 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<BlockResponseDto> getBlockList(Long memberId, Pageable pageable) {

        Page<Block> blocks = memberRepository.findBlockByMemberId(memberId, pageable);

        List<BlockResponseDto> dtos = blocks.stream()
                .map(block -> BlockResponseDto.createDto(block.getId(), block.getToMember().getId(), block.getToMember().getNickname()))
                .toList();

        return new PageImpl<>(dtos, pageable, blocks.getTotalElements());
    }

    /** 관리자 권한 승인 */
    @Transactional
    public void promotionAdmin(Long memberId) {
        Member member = validationService.validateMemberById(memberId);
        member.promotionAdmin();
    }

    /** 관리자 권한 해제 */
    @Transactional
    public void relegationAdmin(Long memberId) {
        Member member = validationService.validateMemberById(memberId);
        member.relegationAdmin();
    }

    /** 사용자 게시글, 댓글 사용 권한 중지 */
    @Transactional
    public void suspendedArticle(Long memberId) {
        Member member = validationService.validateMemberById(memberId);
        member.suspendedArticle();
    }

    /** 사용자 게시글, 댓글 사용 권한 재부여 */
    @Transactional
    public void restartArticle(Long memberId) {
        Member member = validationService.validateMemberById(memberId);
        member.restartArticle();
    }
}
