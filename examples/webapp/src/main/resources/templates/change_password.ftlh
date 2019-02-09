<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.changepw.title' 'Change Password' />
  </#assign>
    
  <#assign content in ui>
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h3><@f.i18n 'page.changepw.content' 'Change Password' /></h3>
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
            <@f.form '/change_password'>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label><@f.i18n 'page.changepw.currentPassword' 'Current Password' /></label>
                  
                  <@f.formPasswordInput 'form.oldPassword' 'form-control' 'err form-control' 'placeholder="${f.fi18n("page.changepw.currentPassword", "Current Password")}" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label><@f.i18n 'page.changepw.newPassword' 'New Password' /></label>
                  <@f.formPasswordInput 'form.password' 'form-control' 'err form-control' 'placeholder="${f.fi18n("page.changepw.newPassword", "New Password")}" maxlength="50" autocomplete="off"' />
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
                  <button type="submit" class="btn btn-success btn-lg" style="width:150%;"><@f.i18n 'page.changepw.save' 'Save' /></button>
                </div>
              </div>
            </@f.form>
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
