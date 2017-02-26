<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Sign In  
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
  
  <#assign content in ui>
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h3>Login Form</h3>
            <hr class="star-primary">
          </div>
        </div>
        
        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <@f.showErrorsBlock />
          </div>
        </div>
        
        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <@f.form '/signin'>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Email Address</label>
                  <@f.formInput 'form.email' 'form-control' 'err form-control' 'placeholder="Email Address" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label>Password</label>
                  <@f.formPasswordInput 'form.password' 'form-control' 'err form-control' 'placeholder="Password" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="checkbox">
                <label for="rememberMe" style="color:grey;">
                  <@f.formCheckbox 'form.rememberMe' />
                  <@f.messageText 'signin.rememberMe' 'Запомнить меня' />
                </label>
              </div>
              <div class="row">
                <div class="form-group col-xs-2">
                  <button type="submit" class="btn btn-success btn-lg" style="width:150%;">Submit</button>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="form-group col-xs-4 col-lg-offset-9">
                  <a href="<@f.url 'signup' />"><@f.messageText 'signup' 'Регистрация'/></a>
                  <span style="color:grey;">&nbsp;|&nbsp;</span>
                  <a href="<@f.url 'restore' />"><@f.messageText 'forgot_password' 'Забыли пароль?'/></a>
                </div>
              </div>
            </@f.form>
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
