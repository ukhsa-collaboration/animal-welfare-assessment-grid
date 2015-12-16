var tableServices = angular.module('tableServices', ['appConfigModule']);

tableServices.factory('tableService', ['$filter','appConfig', 
function($filter, appConfig) {
	function filterData(data, filter) {
		return $filter('filter')(data, filter);
	}

	function orderData(data, params) {
		return params.sorting() ? $filter('orderBy')(data, params.orderBy()) : filteredData;
	}

	function sliceData(data, params) {
		return data.slice((params.page() - 1) * params.count(), params.page() * params.count());
	}

	function transformData(data, filter, params) {
		return sliceData(orderData(filterData(data, filter), params), params);
	}

	var service = {
		pagingOptions : {			
			offset : appConfig.config.defaultPageOffsetStart,
			limit : appConfig.config.defaultPageSize,
			include : appConfig.services.pagingParamValues.metadata
		},
		getSimpleData : function($defer, params, filter, simpleData) {
			var filteredData = filterData(simpleData, filter);
			var transformedData = sliceData(orderData(filteredData, params), params);
			params.total(filteredData.length);
			$defer.resolve(transformedData);
		},
		getLikeData : function($defer, params, filter, fnGetLikeData) {
			if (filter) {
				if (filter.$ === "" || filter.$.length === 1) {
					$defer.resolve([]);
					return;
				} else {
				 	var page = params.page();
				 	var count = params.count();
				 	this.pagingOptions.offset = (page - 1) * count;				 	
				 	this.pagingOptions.limit = count;
					fnGetLikeData(function(data, metadata) {
						if(data.length===0){
							params.page(1);
						}
						params.total(metadata.total);
						$defer.resolve(data);
					}, filter.$, this.pagingOptions);
				}
			}			
		}
	};
	return service;
}]);
