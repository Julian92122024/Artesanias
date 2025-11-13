package com.artesania.santander.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.artesania.santander.entity.Usuario;
import com.artesania.santander.interfaces.IUsuarioDAO;

@Service
public class userDetailsServiceImpl implements UserDetailsService {
	
	//Esta clase nos permite recolectar el username, password y rol del usuario mediante un objeto UserBuilder
	
		@Autowired
		private IUsuarioDAO usuarioDao;

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
			Usuario usuario = usuarioDao.findByUsername(username);
			
			UserBuilder builder = null;
			
			if (usuario != null) {
				
				builder = User.withUsername(username);
				builder.disabled(false);
				builder.password(usuario.getPassword());
				builder.authorities(new SimpleGrantedAuthority("ROLE_USER"));
				
			}
			else {
				throw new UsernameNotFoundException("Usuario No Encontrado");
			}
			
			return builder.build();
		}

}
