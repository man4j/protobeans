<#macro popup>
  <@ui.addScript "connectionPopup">
    <script>
      $(document).ready(function() {
        $(".connectButton").click(function(event){
          event.preventDefault();
          
          var w = 900;
          var h = 600;
          
          var left = (screen.width/2)-(w/2);
          var top = (screen.height/2)-(h/2);
          
          window.open("", "connectWindow", 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
          
          var cTP = $(event.currentTarget).parent();    
          
          cTP[0].setAttribute("target", "connectWindow");
          cTP[0].submit();
        });
      });
    </script>
  </@ui.addScript>
</#macro>

<#macro facebookConnect icon size>
  <@popup />
  
  <@f.form '/auth/facebook'>
    <input type="hidden" name="scope" value="public_profile,email,user_friends" />
    
    <a class="connectButton" href="#">
      <img style="width:${size};" src="<@u.r icon />" alt="Facebook" />
    </a>
  </@f.form>
</#macro>

