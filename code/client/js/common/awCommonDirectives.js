var awCommonDirectivesModule = angular.module('awCommonDirectives', ['graphDrawServices','formatServices', 'graphUtilsModule']);

awCommonDirectivesModule.directive('awErrorDisplay', function() {
    return{
        templateUrl: 'partials/common/error.html',
        restrict: 'E',
        replace : true,
        scope : {
            errors : '='
        }
    };
});

awCommonDirectivesModule.directive('awSuccessDisplay', function($timeout) {
    return{
        templateUrl: 'partials/common/success.html',
        restrict: 'E',
        replace : true,
        scope : {
            success: '='
        },
        link : function($scope){
            $scope.$watch('success', function(newValue, oldValue, scope) {
                if(newValue){
                    $timeout(function(){
                        $scope.success = false;
                    }, 2000)
                }
            });
        }
    };
});


awCommonDirectivesModule.directive('awDatePicker', function() {
    return {
        restrict : 'E',
        replace : true,
        scope : {
            elemId : '@',
            todayHighlight : '=?',
            controller : '='
        },
        template : '<div class="input-group"><input type="text" autocomplete="nope" class="form-control datepicker input-sm" placeholder="Select a date..."">' +
            '<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>',
        link : function($scope, $element)
        {
            $element.children('input').attr('id', $scope.elemId);

            jQuery('#' + $scope.elemId).datepicker({
                format : 'dd-mm-yyyy',
                todayHighlight : $scope.todayHighlight === undefined ? true : $scope.todayHighlight,
                autoclose : true
            });

            $element.on('changeDate', function(ev)
            {
                $scope.controller.onDateChange($('#' + $scope.elemId).datepicker('getUTCDate'));
            });
        }
    };
});


awCommonDirectivesModule.directive('awUploadFile', function() {
    return {
        //templateUrl: 'partials/common/file-upload.html',
        restrict: 'E',
        template : '<input type="file" class="form-control">',
        scope : {
            elemId : '@',
            controller : '='
        },
        link: function($scope, $element) {

            $element.attr('id', $scope.elemId);
                //$element.children('input').attr('id', $scope.elemId);    //    TODO

            $element.on('change', function(e) {
                $scope.controller.onFileUploadChange(e);
            });

        }
    };

});


awCommonDirectivesModule.directive('awSelect2', function() {
    return {
        restrict : 'E',
        replace : true,
        template : '<input type="hidden" class="form-control input-sm">',
        scope : {
            elemId : '@',
            controller : '='
        },
        link : function($scope, $element)
        {
            $element.attr('id', $scope.elemId);

            $element.on('change', function(e)
            {
                $scope.controller.onSelect2Change(e.val, $scope.elemId);
            });

            $scope.controller.onSelect2DirectiveLinked($scope.elemId);
        }
    };
});

awCommonDirectivesModule.directive('awSearchSelect2', function() {
	return {
        restrict : 'E',
        replace : true,
        template : '<input type="hidden" class="form-control input-sm">',
        scope : {
            elemId : '@',
            controller : '='
        },
        link : function($scope, $element)
        {
            $element.attr('id', $scope.elemId);

			$scope.controller.onSearchSelect2DirectiveLinked($scope.elemId);

            $element.on('select2-selecting', function(e)
            {
               	$scope.controller.onSearchSelect2Selecting(e.choice, $scope.elemId);
            });

            $element.on('select2-removed', function(e)
            {
               	$scope.controller.onSearchSelect2Removed(e.choice, $scope.elemId);
            });
        }
    };
});

awCommonDirectivesModule.directive('awLineGraph', ['graphDrawService', 'graphUtils', '$window', function(graphDrawService, graphUtils, $window) {
    return {
        restrict : 'E',
        template : '<div class="col-md-12"></div>',
        replace : true,
        scope : {
            data:'=',
            click:'=',
            hover:'=',
            refresh : '='
        },
        link: function($scope, $element, iAttrs, controller) {

            var container = jQuery($element[0]).parent().parent();
            var options = {
                dimension : {
                    height : 0,
                    width : 0,
                    aspect : 0.8
                },
                margin : {
                    top : 20,
                    bot : 20,
                    left : 30,
                    right : 35
                }
            };

            var __drawTimeline = function(elem, data, options, click, hover) {
                if(angular.isDefined(data) && angular.isDefined(data.length)){
                    if(data.length > 0) {
                        options.dimension = graphUtils.getContainerDimension(container);
                        if(options.dimension.height >0 && options.dimension.width >0){
                            graphDrawService.drawTimeline(elem, data, options, click, hover);
                        }
                    }
                }
            };

            $scope.$watch('data', function(newValue, oldValue) {
                if(angular.equals(newValue,{}) && oldValue.length > 0) {
                    graphDrawService.removeSvg($element[0].childNodes[0]);
                }else{
                    __drawTimeline($element[0], $scope.data, options, $scope.click, $scope.hover);
                }
            });

            jQuery($window).on('resize', function(){
                var svgElem = $element[0].childNodes[0];
                if(angular.isDefined(svgElem)) {
                    graphDrawService.removeSvg(svgElem);
                }
                __drawTimeline($element[0], $scope.data, options, $scope.click, $scope.hover);
            });

            //fix this being run straight away
            $scope.$watch('refresh', function(newValue, oldValue) {
                var svgElem = $element[0].childNodes[0];
                if(newValue){
                    if(angular.isDefined(svgElem)) {
                        graphDrawService.removeSvg(svgElem);
                    }
                    __drawTimeline($element[0], $scope.data, options, $scope.click, $scope.hover);
                }
            });
        }
    };
}]);

awCommonDirectivesModule.directive('awRadarChart', ['graphDrawService','formatService', '$window', 'graphUtils', function(graphDrawService, formatService, $window, graphUtils) {
    return {
        restrict : 'E',
        template : '<div class="col-md-12"></div>',
        replace : true,
        scope : {
            data : '=',
            click : '=',
            hover : '=',
            refresh : '='
        },
        link: function($scope, $element, iAttrs, controller) {

            var container = jQuery($element[0]);
            var options = {};

            var __drawRadarChart = function(elem, data, options, click){
                options.dimension = graphUtils.getContainerDimension(container);
                if(options.dimension.width > 0 && data.length > 0){
                    graphDrawService.drawRadarChart(elem, data, options, click);
                }
            }

            $scope.$watch('data', function(newValue, oldValue) {
                if(newValue.length >0){
                     __drawRadarChart($element[0], $scope.data, options, $scope.click);
                 }
            }, true);

           jQuery($window).on('resize', function(){
                var svgElem = $element[0].childNodes[0];
                graphDrawService.removeSvg(svgElem);
                __drawRadarChart($element[0], $scope.data, options, $scope.click);
           });

           $scope.$watch('refresh', function(newValue, oldValue) {
                var svgElem = $element[0].childNodes[0];
                if(newValue){
                    graphDrawService.removeSvg(svgElem);
                    __drawRadarChart($element[0], $scope.data, options, $scope.click);
                }
           },true);
         }
    };
}]);

awCommonDirectivesModule.directive('awAnimalAssessmentForm', function(){
    return {
        templateUrl: 'partials/assessment-form-template.html',
        restrict: 'E',
        replace : true,
        link : function($scope, element, attrs)
        {
            var parentCtrl = $scope[attrs.parentCtrl];
            if (parentCtrl) {
               $scope.animalCtrl.registerParentController(parentCtrl);
            }
        }
    };
});

