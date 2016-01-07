package ujaen.git.ppt.smtp;
// Se definen los fineros SMTP.
// SP, CRLF, ENDMSG.
// El servidor debe incluir en el correo las cabeceras Message-ID y Received, según la RFC 5322.
//  Los correos se reciben en archivos txt en carpetas diferentes para cada usuario.
// Los ficheros deben seguir el formato de la RFC 5322. 
// Para almacer la clave de usuario, se crea en el directorio de usuario un fichero user.key, 
// donde user es el ID del usuario. Sólo puede contener una línea con una cabecera sin espacios que señala la clave.

public interface RFC5322 {
		
	public static final String SP		=" ";
	public static final String CRLF		="\r\n";
	public static final String ENDMSG	=".\r\n";
	

}
