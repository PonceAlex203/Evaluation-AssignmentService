package microservice.SecurityComponent.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final ThreadLocal<Long> currentAccountId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentRoles = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        Logger.info(getClass(), "Iniciando filtro JwtRequestFilter para: " + path);

        try {
            String accountIdHeader = request.getHeader("X-Account-Id");
            String rolesHeader = request.getHeader("X-User-Roles");

            Logger.info(getClass(), "Headers recibidos: X-Account-Id=" + accountIdHeader + ", X-User-Roles=" + rolesHeader);

            if (accountIdHeader != null && !accountIdHeader.isBlank()) {
                try {
                    Long accountId = Long.parseLong(accountIdHeader);
                    currentAccountId.set(accountId);
                } catch (NumberFormatException e) {
                    Logger.error(getClass(), "Formato inválido de X-Account-Id: " + accountIdHeader);
                }
            }

            if (rolesHeader != null && !rolesHeader.isBlank()) {
                currentRoles.set(rolesHeader);

                // Convertir roles a lista de autoridades (Spring exige "ROLE_" como prefijo)
                List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                        .map(String::trim)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                // Crear autenticación simulada
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(accountIdHeader, null, authorities);

                // Registrar en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

                Logger.info(getClass(), "Usuario autenticado con roles: " + authorities);
            }

            // Continuar el flujo normalmente (pasa al controlador)
            filterChain.doFilter(request, response);

            Logger.info(getClass(), "El flujo regresó del controlador sin errores.");

        } finally {
            // Limpieza después de procesar la request
            SecurityContextHolder.clearContext();
            currentAccountId.remove();
            currentRoles.remove();
            Logger.info(getClass(), "ThreadLocal y contexto de seguridad limpiados.");
        }
    }

    public static Long getCurrentAccountId() {
        return currentAccountId.get();
    }

    public static String getCurrentRoles() {
        return currentRoles.get();
    }
}
