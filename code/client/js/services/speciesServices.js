var speciesServices = angular.module('speciesServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

speciesServices.factory('speciesService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
  function(appConfig, pagingUtils, newDataService, webApiConfig) {

    var URLS = webApiConfig.webApiUrls.entity.species;
    var SPECIES_NAME_KEY = 'speciesName';
    var SPECIES_ID_KEY = 'speciesId';

    var getSpeciesLike = function(callback, likeTerm, pagingOptions) {
      var parameters = {};

      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], SPECIES_NAME_KEY, SPECIES_ID_KEY);
        callback(data.species, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);
    };

    var saveSpecies = function(species, successCallback, errCallback) {
      var onSuccess = function(data) {
        var response = {};
        response[SPECIES_NAME_KEY] = data.value;
        response[SPECIES_ID_KEY] = data.id;
        successCallback(response);
      }

      newDataService.httpPost(URLS.create, species, onSuccess, errCallback);
    };

    var updateSpecies = function(species, successCallback, errCallback) {
      var url = URLS.update + species.speciesId;
      newDataService.httpPut(url, species, successCallback, errCallback);
    };

    var uploadSpecies = function(data, successCallback, errCallback) {
      var url = URLS.upload;
      newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
    };    

    return {
      getSpeciesLike: getSpeciesLike,
      saveSpecies: saveSpecies,
      updateSpecies: updateSpecies,
      uploadSpecies : uploadSpecies
    };
  }
]);