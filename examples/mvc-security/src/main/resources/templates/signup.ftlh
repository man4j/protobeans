<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.signup.title' 'SignUp Page'/> 
  </#assign>
    
  <#assign content in ui>
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h3><@f.i18n 'page.signup.form' 'SignUp Form'/></h3>
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
            <@f.form '/signup'>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label><@f.i18n 'page.email' 'Email' /></label>
                  <@f.formInput 'form.email' 'form-control' 'err form-control' 'placeholder="Email" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label><@f.i18n 'page.signin.password' 'Password'/></label>
                  <@f.formPasswordInput 'form.password' 'form-control' 'err form-control' 'placeholder="${f.fi18n("page.signin.password", "Password")}" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label><@f.i18n 'page.changepw.confirmPassword' 'Confirm password' /></label>
                  <@f.formPasswordInput 'form.confirmPassword' 'form-control' 'err form-control' 'placeholder="${f.fi18n("page.changepw.confirmPassword", "Confirm Password")}" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <br />
              <div class="row">
                <div class="form-group col-xs-2">
                  <button type="submit" class="btn btn-success btn-lg" style="width:150%;"><@f.i18n 'page.signin.ok' 'OK' /></button>
                </div>
              </div>
              <hr />
              <div class="row">
                <div class="form-group col-xs-4 col-lg-offset-9">
                  <a href="<@f.url '/signin' />"><@f.i18n 'page.signin.title' 'SignIn page'/></a>
                  <span style="color:grey;">&nbsp;|&nbsp;</span>
                  <a href="#"><@f.i18n 'help' 'Help'/></a>
                </div>
              </div>
            </@f.form>
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
