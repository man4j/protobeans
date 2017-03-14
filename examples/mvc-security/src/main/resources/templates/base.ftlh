<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title><@ui.insert title! /></title>

  <!-- Bootstrap Core CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

  <!-- Theme CSS -->
  <link href="<@u.r 'css/freelancer.min.css' />" rel="stylesheet">

  <!-- Custom Fonts -->
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->

  <link rel="icon" type="image/png" href="<@u.r 'favicon.png' />">
</head>

<body id="page-top" class="index">    
  <nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom" style="padding-top:0px;">
    <div class="container">
      <div class="navbar-header page-scroll" style="padding-top:20px;">
        <a class="navbar-brand" href="/"><@f.i18n 'page.mainTitle' 'Start Here'/></a>
      </div>

      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="padding-top:20px;">
        <ul class="nav navbar-nav navbar-right">
          <@ui.insert navbar! />
          <li>
            <a style="padding-left:30px;" href="<@f.url '?locale=en'/>">
              <img style="width:24px;" src="<@u.r 'img/en.png' />" />
            </a>
          </li>
          <li>
            <a style="padding-left:0px;padding-right:0px;" href="<@f.url '?locale=ru'/>">
              <img style="width:24px;" src="<@u.r 'img/ru.png' />" />
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <@ui.insert content! />

  <!-- Footer -->
  <footer class="text-center">
    <div class="footer-above">
      <div class="container">
        <div class="row">
          <div class="footer-col col-md-4">
            <h3>Location</h3>
            <p>3481 Melrose Place
              <br>Beverly Hills, CA 90210
            </p>
          </div>
          <div class="footer-col col-md-4">
            <h3>Around the Web</h3>
            <ul class="list-inline">
              <li>
                <a href="#" class="btn-social btn-outline"><i class="fa fa-fw fa-facebook"></i></a>
              </li>
              <li>
                <a href="#" class="btn-social btn-outline"><i class="fa fa-fw fa-google-plus"></i></a>
              </li>
              <li>
                <a href="#" class="btn-social btn-outline"><i class="fa fa-fw fa-twitter"></i></a>
              </li>
              <li>
                <a href="#" class="btn-social btn-outline"><i class="fa fa-fw fa-linkedin"></i></a>
              </li>
              <li>
                <a href="#" class="btn-social btn-outline"><i class="fa fa-fw fa-dribbble"></i></a>
              </li>
            </ul>
          </div>
          <div class="footer-col col-md-4">
            <h3>About</h3>
            <p>Some usefull info</p>
          </div>
        </div>
      </div>
    </div>
    <div class="footer-below">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            Copyright &copy; Your Website 2016
          </div>
        </div>
      </div>
    </div>
  </footer>

  <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
  <div class="scroll-top page-scroll hidden-sm hidden-xs hidden-lg hidden-md">
    <a class="btn btn-primary" href="#page-top">
      <i class="fa fa-chevron-up"></i>
    </a>
  </div>
  
  <!-- jQuery -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

  <!-- Bootstrap Core JavaScript -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <!-- Plugin JavaScript -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>

  <!-- Theme JavaScript -->
  <script src="<@u.r 'js/freelancer.min.js' />"</script>
  
  <@ui.insertScripts />
</body>
</html>
