Delimiters:

<script language="javascript">
function tralala {var int}

</script>

<script charset="sdd" type="text/javascript">
function tralala {var int}

</script>

<style type="te xt / c s s">
body { color: "white"; }
</style>

<start_tag>
</end_tag> 
<!--comment-->
<%code%>
<%@directive name=value %>
/* multiline comment */

Ierarhie:
<
	>
</
	>
<!--
	-->
<%@ 
	%>
<%
	%>
	/*
		*/
		%>
		
=========================
Initial script_start_del default_script_content script_comment default_script_content script_end_del:
<%
	public void inc(int i) {
		i++;
	/*	return 0;  */
	}
%>

Modif:
insert "%>"

<%
	public void inc(int i) {
		i++; %>
	/*	return 0;  */
	}
%>

* Reparsarea ar trebui sa se faca de la "<%", nu de la "<*" 
