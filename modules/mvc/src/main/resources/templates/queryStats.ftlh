<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Hibernate query stats</title>

    <link href="<@u.r 'admin/vendor/bootstrap/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<@u.r 'admin/vendor/metisMenu/metisMenu.min.css'/>" rel="stylesheet">
    <link href="<@u.r 'admin/vendor/datatables-plugins/dataTables.bootstrap.css'/>" rel="stylesheet">
    <link href="<@u.r 'admin/vendor/datatables-responsive/dataTables.responsive.css'/>" rel="stylesheet">
    <link href="<@u.r 'admin/dist/css/sb-admin-2.css'/>" rel="stylesheet">
    <link href="<@u.r 'admin/vendor/font-awesome/css/font-awesome.min.css'/>" rel="stylesheet" type="text/css">
</head>

<body>
    <div id="wrapper">
        <div id="page-wrapper" style="margin:0px;">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Hibernate query statistics
                        </div>
                        <div class="panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Query</th>
                                        <th>Min</th>
                                        <th>Max</th>
                                        <th>AVG</th>
                                        <th>Count</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <#if queryList??>
	                                  <#list queryList as item>  
	                                    <tr class="odd gradeX">
	                                        <td>${item.query}</td>
	                                        <td>${item.minTime} ms</td>
	                                        <td>${item.maxTime} ms</td>
	                                        <td>${item.avgTime} ms</td>
	                                        <td>${item.count}</td>
	                                    </tr>
	                                  </#list>
	                                </#if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="<@u.r 'admin/vendor/jquery/jquery.min.js'/>"></script>
    <script src="<@u.r 'admin/vendor/bootstrap/js/bootstrap.min.js'/>"></script>
    <script src="<@u.r 'admin/vendor/metisMenu/metisMenu.min.js'/>"></script>
    <script src="<@u.r 'admin/vendor/datatables/js/jquery.dataTables.min.js'/>"></script>
    <script src="<@u.r 'admin/vendor/datatables-plugins/dataTables.bootstrap.min.js'/>"></script>
    <script src="<@u.r 'admin/vendor/datatables-responsive/dataTables.responsive.js'/>"></script>
    <script src="<@u.r 'admin/dist/js/sb-admin-2.js'/>"></script>
    <script>
    $(document).ready(function() {
        $('#dataTables-example').DataTable({
            responsive: true,
            "order": [[ 3, "desc" ]]            
        });
    });
    </script>
</body>
</html>
