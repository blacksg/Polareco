package ecoders.polareco.auth.oauth2.handler;

import ecoders.polareco.auth.jwt.service.JwtService;
import ecoders.polareco.member.entity.Member;
import ecoders.polareco.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("client-url")
    private String clientUrl;

    private final JwtService jwtService;

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        // 인증 성공한 Google 사용자 정보 획득
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 이메일 주소에 해당하는 데이터를 조회한 결과에 따라 그대로 사용하거나 새로 생성 및 저장
        String email = (String) attributes.get("email");
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            String username = (String) attributes.get("name");
            String profileImage = (String) attributes.get("picture");
            Member createdMember = new Member(email, username);
            createdMember.setProfileImage(profileImage);
            memberRepository.save(createdMember);
            member = createdMember;
        }

        // 회원 데이터를 가지고 JWT 토큰 발급
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String accessToken = jwtService.issueAccessToken(member);
        String refreshToken = jwtService.issueRefreshToken(member);

        // 리디렉팅할 프론트엔드 URI에 토큰을 쿼리 파라미터로 전달
        String url = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("polareco-deploy.s3-website.ap-northeast-2.amazonaws.com")
            .path("/login")
            .queryParam("accessToken", accessToken)
            .queryParam("refreshToken", refreshToken)
            .build()
            .toString();
        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
