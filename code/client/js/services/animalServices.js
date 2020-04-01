var animalServices = angular.module('animalServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

animalServices.factory('animalService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
  function(appConfig, pagingUtils, newDataService, webApiConfig) {

    var URLS = webApiConfig.webApiUrls.entity.animal;
    var FEMALE_ANIMAL_FILTER = 'f';
    var MALE_ANIMAL_FILTER = 'm';
    var ANIMAL_NUMBER_KEY = 'animalNumber';
    var ANIMAL_ID_KEY = 'id';

    var getAnimalsLike = function(successCallback, likeTerm, pagingOptions) {
      var parameters = {};
      
      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], ANIMAL_NUMBER_KEY, ANIMAL_ID_KEY);
        successCallback(data.animals, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);
    };

    var _getSexFilteredAnimalsLike = function(successCallback, likeTerm, sex, pagingOptions) {
      var parameters = {};
      
      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], ANIMAL_NUMBER_KEY);
        successCallback(data.animals, metadata);
      };

      var url = sex === FEMALE_ANIMAL_FILTER ? URLS.getFemaleLike : URLS.getMaleLike;

      newDataService.httpGet(url, parameters, onSuccess);
    };

    var getFemaleAnimalsLike = function(callback, likeTerm, pagingOptions) {
      _getSexFilteredAnimalsLike(callback, likeTerm, FEMALE_ANIMAL_FILTER, pagingOptions);
    };

    var getMaleAnimalsLike = function(callback, likeTerm, pagingOptions) {
      _getSexFilteredAnimalsLike(callback, likeTerm, MALE_ANIMAL_FILTER, pagingOptions);
    };

    var getAnimalById = function(successCallback, animalId) {
      var parameters = {};
      var url = URLS.getById + animalId;
      newDataService.httpGet(url, parameters, successCallback);
    };

    var saveAnimal = function(animal, successCallback, errCallback) {
      newDataService.httpPost(URLS.create, animal, successCallback, errCallback);
    };

    var updateAnimal = function(animal, successCallback, errCallback) {
      var url = URLS.update + animal.id;
      newDataService.httpPut(url, animal, successCallback, errCallback);
    };

    var uploadAnimal = function(data, successCallback, errCallback) {
      var url = URLS.upload;
      newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
    };    

    var removeAnimal = function(animal, successCallback, errCallback) {
      var url = URLS.delete + animal.id;
      newDataService.httpDelete(url, successCallback, errCallback);      
    };

    var getBasicAnimal = function(id, animalNumber) {
      return {
        id: id,
        number: animalNumber
      };
    };

    return {
      getFemaleAnimalsLike: getFemaleAnimalsLike,
      getMaleAnimalsLike: getMaleAnimalsLike,
      getAnimalsLike: getAnimalsLike,
      getAnimalById: getAnimalById,
      saveAnimal: saveAnimal,
      updateAnimal: updateAnimal,
      removeAnimal: removeAnimal,
      getBasicAnimal: getBasicAnimal,
      uploadAnimal : uploadAnimal
    };
  }
]);