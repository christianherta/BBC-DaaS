package de.bbcdaas.themehandlerweb.service.membership;

import de.bbcdaas.themehandlerweb.beans.membership.UserDetailsImpl;
import de.bbcdaas.themehandlerweb.dao.ThemeHandlerWebDao;
import de.bbcdaas.themehandlerweb.domains.UserEntity;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Robert Illers
 */
@Service("userDetailsService") 
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired private ThemeHandlerWebDao dao;

	/**
	 * 
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws DataAccessException 
	 */
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		UserDetailsImpl userDetails;
		UserEntity userEntity = dao.getUserByName(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException("user not found");
		}
		String userName = userEntity.getLoginName();
		String password = userEntity.getPassword();
		Integer userRole = userEntity.getUserRole(); 
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		switch(userRole) {
			case 1:
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				break;
			default:
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		userDetails = new UserDetailsImpl(userName, password, grantedAuthorities);
		return userDetails;
	}
}
