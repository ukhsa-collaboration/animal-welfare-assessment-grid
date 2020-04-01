window.awconfig = {
    // Reverse proxy maps the two URLs below
    // serverUrl : 'http://localhost:8080/animal-welfare-system/'
    // Replace_Token_Start
    serverUrl: 'http://localhost/animal-welfare-system-client/server/',
    // Replace_Token_End
};

(function () {
    var config = window.awconfig;
    config.resourcesUrl = config.serverUrl + 'resources/';
    config.webapiUrl = config.serverUrl + 'webapi';

    var systemApi = {};
    systemApi.baseUrl = config.webapiUrl + '/system';
    systemApi.authenticationApiBase = systemApi.baseUrl + '/auth';
    systemApi.authenticationCheck = systemApi.authenticationApiBase + '/auth-check';
    systemApi.getLogonDetails = systemApi.authenticationApiBase + '/logondetails';
    systemApi.logout = systemApi.authenticationApiBase + '/logout';

    config.systemApi = systemApi;
})();