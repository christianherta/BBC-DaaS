package de.bbcdaas.themehandlerweb.beans.membership;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Robert Illers
 */
public class UserDetailsImpl implements UserDetails {

	private String userName;
	private String password;
	private Collection<GrantedAuthority> grantedAuthorities;
	
	/**
	 * 
	 * @param userName 
	 */
	public UserDetailsImpl(String userName, String password, Collection<GrantedAuthority> grantedAuthorities) {
		
		this.userName = userName;
		this.password = password;
		this.grantedAuthorities = grantedAuthorities;
	}
	
	/**
	 * 
	 * @return 
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.grantedAuthorities;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public String getUsername() {
		return this.userName;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
