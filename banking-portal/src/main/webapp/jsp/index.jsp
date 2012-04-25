<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" 
%><%@taglib uri="http://www.springframework.org/tags/form" prefix="form"
%><%@taglib uri="http://www.springframework.org/tags" prefix="spring"
%><div align="center">
	<form name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
		<div id ="login_holder">
			<div id ="login_area">				
				<table width="350px">
					<tr>
						<td colspan="2" style="text-align: right;">
						<!-- Änderung der Sprache -->
						<a href="?lang=en"><img src="<c:url value='/media/images/flag_en.png'/>" alt="English"></a>     			
					    <a href="?lang=de"><img src="<c:url value='/media/images/flag_de.png'/>" alt="German"></a>
						</td>
					</tr>
					<tr>				        
				       <td colspan="2" ><span style="font-size: 13px; font-weight: 700; border-bottom: 1px dotted #ccc; display: block;">Spring3 advanced example - <spring:message code="text.login"/></span><br />
				       <c:if test="${not empty param.login_error}">
					      <font color="red">
					        <spring:message code="text.failureloginmessage"/>.<br/>
					        <spring:message code="text.grund"/>: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
					      </font>
		    		   </c:if>				       				      
				       </td>				       
				    </tr>
				    <tr>
				        <td><spring:message code="label.benutzername"/></td>
				        <td><input type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/></td>
				    </tr>
				    <tr>
				        <td><spring:message code="label.passwort"/></td>
				        <td><input type='password' name='j_password' /></td>
				    </tr>
				     <tr>
				        <td></td>
				        <td><input type="checkbox" name="_spring_security_remember_me"><spring:message code="label.wiedererkennen"/></td>
				    </tr>
				    <tr>
				        <td></td>
				        <td>
				            <input type="submit" type="submit" value="<spring:message code="label.anmelden"/>"/>
				            <br />
				        </td>
			    	</tr>
				</table>				
			</div>
		</div>
	</form>
</div>