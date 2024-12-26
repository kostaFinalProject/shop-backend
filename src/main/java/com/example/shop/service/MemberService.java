package com.example.shop.service;

import com.example.shop.config.CustomOAuth2UserService;
import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import com.example.shop.dto.login.LoginDto;
import com.example.shop.dto.login.OAuthLoginDto;
import com.example.shop.dto.member.*;
import com.example.shop.dto.instagram.article.ArticleSummaryResponseDto;
import com.example.shop.repository.member.MemberRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private final ImageService imageService;
    private final TokenService tokenService;
    private final CustomOAuth2UserService customOAuth2UserService;

    /** 회원 가입 */
    @Transactional
    public void saveMember(MemberSignUpDto dto) {
        if (memberRepository.duplicateMember(dto.getUserId(), dto.getNickname(), dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 ID이거나 사용중인 닉네임이거나 이메일입니다.");
        }

        Address address = Address.createAddress(dto.getPostCode(), dto.getRoadAddress(), dto.getRoadAddress());

        Member member = Member.createMember(dto.getUserId(), bCryptPasswordEncoder.encode(dto.getPassword()),
                dto.getName(), dto.getNickname(), dto.getEmail(),
                dto.getPhone(), address, Grade.valueOf(dto.getGrade()), Provider.valueOf(dto.getProvider()));

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

    /** 회원 정보 조회 */
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(Long memberId) {
        Member findMember = validationService.validateMemberById(memberId);

        String phone = findMember.getPhone();
        String postCode = null;
        String roadAddress = null;
        String detailAddress = null;

        if (findMember.getAddress() != null) {
            postCode = findMember.getAddress().getPostCode();
            roadAddress = findMember.getAddress().getRoadAddress();
            detailAddress = findMember.getAddress().getDetailAddress();
        }

        return MemberResponseDto.createDto(findMember.getId(), findMember.getName(), findMember.getNickname(),
                findMember.getEmail(), phone, postCode,
                roadAddress, detailAddress, findMember.getGrade().name(), findMember.getPointGrade().name(),
                findMember.getPoints(), findMember.getPayment());
    }

    /** 회원의 게시물 조회 (프로필 비공개 시 팔로우 해야만 조회 가능) */
    @Transactional(readOnly = true)
    public Page<ArticleSummaryResponseDto> getArticle(Long targetMemberId, Long fromMemberId, Pageable pageable) {

        Page<Article> articles = memberRepository.findArticleByMemberId(targetMemberId, fromMemberId, pageable);

        List<ArticleSummaryResponseDto> dtos = articles.stream()
                .map(article -> {
                    Long likeId = validationService.findArticleLikeIdByArticleAndMember(article.getId(), fromMemberId);

                    String memberProfileImageUrl = null;
                    if (article.getMember().getMemberProfileImg() != null) {
                        memberProfileImageUrl = article.getMember().getMemberProfileImg().getImgUrl();
                    }

                    List<String> hashTags = article.getArticleTags().stream()
                            .map(articleTag -> articleTag.getTag().getTag())
                            .toList();

                    return ArticleSummaryResponseDto.createDto(article.getId(), targetMemberId,
                            article.getMember().getNickname(), memberProfileImageUrl,article.getArticleImages().get(0).getImgUrl(),
                            article.getContent(), article.getLikes(), article.getViewCounts(), likeId, hashTags);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, articles.getTotalElements());
    }

    /** 회원의 태그 상품 조회 */
    public Page<ArticleItemResponseDto> getArticleItems(Long targetMemberId, Long fromMemberId, Pageable pageable) {
        Page<Item> items = memberRepository.findTaggedItemsByMemberId(targetMemberId, fromMemberId, pageable);

        List<ArticleItemResponseDto> dtos = items.stream()
                .map(item ->
                        ArticleItemResponseDto.createDto(item.getId(), item.getName(), item.getRepItemImage()))
                .toList();

        return new PageImpl<>(dtos, pageable, items.getTotalElements());
    }

    /** 회원의 프로필 이미지 조회 */
    @Transactional(readOnly = true)
    public MemberProfileResponseDto getMemberProfile(Long targetMemberId, Long fromMemberId) {
        Member targetMember = validationService.validateMemberById(targetMemberId);
        String memberProfileImageUrl = null;
        String introduction = null;
        Long followerId = null;
        String follow = "Not Follow";

        if (targetMemberId.equals(fromMemberId)) {
            follow = "Me";
        }

        if (targetMember.getMemberProfileImg() != null) {
            memberProfileImageUrl = targetMember.getMemberProfileImg().getImgUrl();
        }

        if (targetMember.getIntroduction() != null) {
            introduction = targetMember.getIntroduction();
        }

        Follower follower = validationService.findFollowerByFolloweeIdAndFollowerId(fromMemberId, targetMemberId);

        if (follower != null) {
            followerId = follower.getId();
            follow = "Followed";
        }

        return MemberProfileResponseDto.createDto(targetMemberId, targetMember.getNickname(), introduction,
                memberProfileImageUrl, targetMember.getAccountStatus().name(),
                targetMember.getArticles(), targetMember.getFollowees(), targetMember.getFollowers(), followerId, follow);
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
    public Page<FollowerListResponseDto> getFollower(Long memberId, Long fromMemberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowerByMemberId(memberId, fromMemberId, pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> {
                    String memberProfileImageUrl = null;
                    if (follower.getFollower().getMemberProfileImg() != null) {
                        memberProfileImageUrl = follower.getFollower().getMemberProfileImg().getImgUrl();
                    }

                    return FollowerListResponseDto.createDto(follower.getId(),
                            follower.getFollower().getId(), memberProfileImageUrl,
                            follower.getFollower().getNickname(), follower.getFollower().getIntroduction(),
                            follower.getFollowerStatus().name());
                }).toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }

    /** 회원의 팔로워 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<FollowerListResponseDto> getFollowee(Long memberId, Long fromMemberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowingByMemberId(memberId, fromMemberId,pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> {
                    String memberProfileImageUrl = null;
                    if (follower.getFollowee().getMemberProfileImg() != null) {
                        memberProfileImageUrl = follower.getFollowee().getMemberProfileImg().getImgUrl();
                    }

                    return FollowerListResponseDto.createDto(follower.getId(),
                            follower.getFollowee().getId(), memberProfileImageUrl,
                            follower.getFollowee().getNickname(), follower.getFollowee().getIntroduction(),
                            follower.getFollowerStatus().name());
                }).toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }

    /** 팔로우를 신청받은 회원의 팔로우 요청 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<FollowerListResponseDto> getFolloweeRequest(Long memberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowingRequestByMemberId(memberId, pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> {
                    String memberProfileImageUrl = null;
                    if (follower.getFollowee().getMemberProfileImg() != null) {
                        memberProfileImageUrl = follower.getFollowee().getMemberProfileImg().getImgUrl();
                    }

                    return FollowerListResponseDto.createDto(follower.getId(),
                            follower.getFollowee().getId(), memberProfileImageUrl,
                            follower.getFollowee().getNickname(), follower.getFollowee().getIntroduction(),
                            follower.getFollowerStatus().name());
                }).toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }

    /** 회원의 차단 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<BlockResponseDto> getBlockList(Long memberId, Pageable pageable) {

        Page<Block> blocks = memberRepository.findBlockByMemberId(memberId, pageable);

        List<BlockResponseDto> dtos = blocks.stream()
                .map(block -> {
                    String memberProfileImageUrl = null;
                    if (block.getToMember().getMemberProfileImg() != null) {
                        memberProfileImageUrl = block.getToMember().getMemberProfileImg().getImgUrl();
                    }

                    return BlockResponseDto.createDto(block.getId(), block.getToMember().getId(),
                            memberProfileImageUrl, block.getToMember().getNickname(),
                            block.getToMember().getIntroduction());
                })
                .toList();

        return new PageImpl<>(dtos, pageable, blocks.getTotalElements());
    }

    /** 회원 조회 */
    @Transactional(readOnly = true)
    public Page<MemberListResponseDto> getMemberList(String nickname, Long memberId, Pageable pageable) {
        Page<Member> members = memberRepository.searchMember(nickname, memberId, pageable);

        List<MemberListResponseDto> dtos = members.stream()
                .map(member -> {
                    String memberProfileImageUrl = null;
                    if (member.getMemberProfileImg() != null) {
                        memberProfileImageUrl = member.getMemberProfileImg().getImgUrl();
                    }
                    return MemberListResponseDto.createDto(member.getId(), member.getNickname(), memberProfileImageUrl);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, members.getTotalElements());
    }

    /** 로그인 */
    @Transactional(readOnly = true)
    public String login(LoginDto loginDto) {
        Member member = memberRepository.findByUserId(loginDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 아이디입니다."));

        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenService.generateAccessToken(member.getUserId());
        String refreshToken = tokenService.generateRefreshToken(member.getUserId());

        return accessToken + ":" + refreshToken;
    }

    @Transactional
    public String oauthLogin(OAuthLoginDto loginDto) {

        String provider = loginDto.getProvider();
        Provider providerEnum = Provider.valueOf(provider.toUpperCase());

        OAuth2User oAuth2User = customOAuth2UserService.loadUserFromProvider(loginDto.getAccessToken(), provider);

        String email = extractEmail(oAuth2User, provider);

        Member member = memberRepository.findByEmailAndProvider(email, providerEnum)
                .orElseGet(() -> customOAuth2UserService.createSocialMember(email, providerEnum));

        String accessToken = tokenService.generateAccessToken(member.getUserId());
        String refreshToken = tokenService.generateRefreshToken(member.getUserId());

        return accessToken + ":" + refreshToken;
    }

    private String extractEmail(OAuth2User oAuth2User, String provider) {
        return switch (provider) {
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                yield kakaoAccount.get("email").toString();
            }
            case "naver" -> {
                Map<String, Object> naverResponse = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                yield naverResponse.get("email").toString();
            }
            case "google" -> oAuth2User.getAttributes().get("email").toString();
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    /** 로그아웃 */
    @Transactional
    public void logout(String token, String refreshToken) {
        tokenService.invalidateToken(token);
        tokenService.invalidateToken(refreshToken);
    }

    /** Access Token 재발급 */
    @Transactional(readOnly = true)
    public String refreshAccessToken(String refreshToken) {
        return tokenService.refreshAccessToken(refreshToken);
    }

    @Transactional(readOnly = true)
    public Page<AdminRequestMemberDto> getAdminRequestMember(Pageable pageable) {
        Page<Member> requestAdminMember = memberRepository.findRequestAdminMember(pageable);

        List<AdminRequestMemberDto> dtos = requestAdminMember.stream()
                .map(member -> AdminRequestMemberDto.createDto(member.getId(), member.getUserId(), member.getGrade().name()))
                .toList();

        return new PageImpl<>(dtos, pageable, requestAdminMember.getTotalElements());
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
    public void enableArticle(Long memberId) {
        Member member = validationService.validateMemberById(memberId);
        member.enableArticle();
    }
}
