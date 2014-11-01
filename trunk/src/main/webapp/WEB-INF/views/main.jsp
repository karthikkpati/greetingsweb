<!DOCTYPE html>
<html>
    <head>
        <title>Durandal</title>
        
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection" content="telephone=no"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        
        <link rel="apple-touch-startup-image" href="resources/lib/durandal/img/ios-startup-image-landscape.png" media="(orientation:landscape)" />
        <link rel="apple-touch-startup-image" href="resources/lib/durandal/img/ios-startup-image-portrait.png" media="(orientation:portrait)" />
        <link rel="apple-touch-icon" href="resources/lib/durandal/img/icon.png"/>
        
        <link rel="stylesheet" href="resources/lib/bootstrap/css/bootstrap.css" />
        <link rel="stylesheet" href="resources/lib/bootstrap/css/bootstrap-responsive.css" />
        <link rel="stylesheet" href="resources/lib/font-awesome/css/font-awesome.css" />
        <link rel="stylesheet" href="resources/css/ie10mobile.css" />
        <link rel="stylesheet" href="resources/lib/durandal/css/durandal.css" />
        <link rel="stylesheet" href="resources/css/samples.css" />
        <link rel="stylesheet" href="resources/css/datepicker.css" />
         <link href="resources/css/app.css" rel="stylesheet" />
	<link href="resources/css/data/slide1.css" rel="stylesheet" />
	<script type="text/javascript" src="resources/lib/jquery/jquery-1.9.1.js"></script>
         <script type="text/javascript" src="http://code.createjs.com/createjs-2013.09.25.min.js"></script>
        <script type="text/javascript">
            if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
                var msViewportStyle = document.createElement("style");
                var mq = "@@-ms-viewport{width:auto!important}";
                msViewportStyle.appendChild(document.createTextNode(mq));
                document.getElementsByTagName("head")[0].appendChild(msViewportStyle);
            }
        </script>
    </head>
    <body>

          <div id="applicationHost">
              <div class="splash">


                 <div class="message">
               Welcome to GreetAPal.com
       
                </div>

    
           

            <i class="icon-spinner icon-2x icon-spin active"></i>


            <div >

                <div class="writer" style="height:100px;">

                
                Loading Application<br />
                    <p>... </p>
                Please Wait  <br />
               </div>
                
            </div>
   </div>



     </div>


    <script type="text/javascript" src="resources/app/Util/writeSimulate.js" >


    
    </script>



   <script type="text/javascript" src="resources/GreetPalApp/Util/ajaxService.js"></script>

  <script src="resources/lib/require/require.js" data-main="resources/GreetPalApp/main"></script>  
    </body>
</html>