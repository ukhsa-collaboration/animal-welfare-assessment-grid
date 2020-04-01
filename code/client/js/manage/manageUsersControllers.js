var manageUsersControllers = angular.module('manageUsersControllers', ['appConfigModule', 'awCommonDirectives', 'userAuthServices', 'formServices', 'logonServices']);

var userMngmtFormEvents = {
    onExistingUserSelected : "onExistingUserSelected",
    onExistingUserGroupUpdate : "onExistingUserGroupUpdate",
    onNewUserSelected : "onNewUserSelected",
    onUserClearSelected : "onUserClearSelected",
    onUserRemoved : "onUserRemoved",
    onUserRemovedClearUpdate : "onUserRemovedClearUpdate",
    onGroupSelected : "onGroupSelected"
};

manageUsersControllers.controller('MngmtUsersSelectionCtrl', ['$scope', 'formService', 'userAuthService', 'appConfig',
function($scope, formService, userAuthService, appConfig)
{
    this.selectId = "userSelect";
    this.placeHolder = "Create/Search a user...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), userAuthService.getAuthUsersLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(userMngmtFormEvents.onNewUserSelected, choice);
        }
        else {
            this.getUserByUsername(choice.id);
        }
    };

    this.getUserByUsername = function(userName)
    {
    	userAuthService.getAuthUserById(userName, this.getUserByUsernameCallback);
    };

    this.getUserByUsernameCallback = function(data)
    {
    	$scope.$emit(userMngmtFormEvents.onExistingUserSelected, data);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(userMngmtFormEvents.onUserClearSelected, choice);
    };

    $scope.$on(userMngmtFormEvents.onUserRemovedClearUpdate, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

manageUsersControllers.controller('MngmtGroupSelectionCtrl', ['$scope', 'appConfig',
function($scope, appConfig)
{
	this.defaultSelectGroup = "Select a group";
	this.selectedGroup = this.defaultSelectGroup;
	this.groups = [
		{id : appConfig.auth.groups.admin.roleName, name : appConfig.auth.groups.admin.userFriendlyName},
	 	{id : appConfig.auth.groups.assessmentuser.roleName, name : appConfig.auth.groups.assessmentuser.userFriendlyName}
	];
	this.selectId = "#groupSelect";
	this.dropDownOpenClass = "open";
	var that = this;

	this.groupSelected = function(group)
	{
		jQuery(this.selectId).removeClass(this.dropDownOpenClass);
		this.selectedGroup = group.name;
		$scope.$emit(userMngmtFormEvents.onGroupSelected, group.id);
	};

	$scope.$on(userMngmtFormEvents.onUserRemovedClearUpdate, function(event)
    {
        that.selectedGroup = that.defaultSelectGroup;
    });

    $scope.$on(userMngmtFormEvents.onExistingUserGroupUpdate, function(event, groupName)
    {
        that.selectedGroup = appConfig.auth.groups[groupName].userFriendlyName;
    });
}]);

manageUsersControllers.controller('MngmtUsersManagementCtrl', ['$scope', 'userAuthService', 'appConfig', '$timeout', 'logonService',
function($scope, userAuthService, appConfig, $timeout, logonService)
{
	this.user = {};
    this.isNewUser = true;
    this.isSuccess = false;
    this.isDeletingUser = false;
    this.errors = [];
    this.isFormViewable = false;
    this.isAdminUser = false;
    var that = this;

   this.resetuserAuthContainer = function(){
		this.user = {
			userName : undefined,
			password : undefined,
			retypedPassword : undefined,
			groupName : undefined
		};
		this.errors = [];
	};

    this.setUserName = function(userName)
    {
        this.user.userName = userName;
    };

    this.setUserGroup = function(groupName)
    {
    	this.user.groupName = groupName;
    };
    
    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.onNewUser = function(choice)
    {
        this.isSuccess = false;
        this.isNewUser = true;
        this.setUserName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingUser = function(user)
    {
        this.isSuccess = false;
        this.isNewUser = false;
        this.setUserName(user.userName);
        this.setUserGroup(user.userGroup);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(userMngmtFormEvents.onExistingUserGroupUpdate, user.userGroup);
        });
    };

    this.onClearUser = function(choice)
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewUser = true;
        this.resetuserAuthContainer();
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(userMngmtFormEvents.onUserRemovedClearUpdate);
        });
    };

    this.clearUser = function()
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewUser = true;
        this.resetuserAuthContainer();
        $scope.$broadcast(userMngmtFormEvents.onUserRemovedClearUpdate);
    };

    this.onGroupSelected = function(groupName)
    {
    	this.setUserGroup(groupName);
    };

    this.submitUser = function()
    {
        userAuthService.saveAuthUser(this.user, submitUserCallback, onErrorCallBack);
    };

    this.updateUser = function()
    {
        userAuthService.updateAuthUser(this.user, updateUserCallback, onErrorCallBack);
    };

    this.startDeleteUser = function()
    {
    	this.isDeletingUser = true;
    };

    this.confirmDeleteUser = function()
    {	
    	this.isDeletingUser = false;
    	userAuthService.deleteAuthUser(this.user, deleteUserCallback, onErrorCallBack);
    };

    this.cancelDeleteUser = function()
    {
    	this.isDeletingUser = false;
    };

    var deleteUserCallback = function()
    {
    	that.isSuccess = true;
    	that.isNewUser = true;
    	that.resetuserAuthContainer();
    	$timeout(function(){
            $scope.$apply();
            $scope.$broadcast(userMngmtFormEvents.onUserRemovedClearUpdate);
        });	
    };
    
    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    };

    var submitUserCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
        that.isNewUser = false;
    };

    var updateUserCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var removeUserCallback = function(data)
    {
        that.isSuccess = true;
        that.isNewUser = true;
        that.resetuserAuthContainer();
        $scope.$broadcast(userMngmtFormEvents.onUserRemoved);
    };

    var getLogonDetailsCallback = function(data)
    {
    	if(data.authType===appConfig.auth.authTypes.database)
    	{
    		that.isFormViewable = true;
    	}
    	else
		{
			that.isFormViewable = false;
		}

		if(data.groupName===appConfig.auth.groups.admin.roleName)
		{
			that.isAdminUser = true;
		}
		else
		{
			that.isAdminUser = false;
		}
    };

	$scope.$on(userMngmtFormEvents.onNewUserSelected, function(event, choice)
    {
        that.onNewUser(choice);
    });

    $scope.$on(userMngmtFormEvents.onExistingUserSelected, function(event, user)
    {
        that.onExistingUser(user);
    });

    $scope.$on(userMngmtFormEvents.onUserClearSelected, function(event, choice)
    {
        that.onClearUser(choice);
    });

    $scope.$on(userMngmtFormEvents.onGroupSelected, function(event, groupId)
    {
        that.onGroupSelected(groupId);
    });

	this.resetuserAuthContainer();
	logonService.getLogonDetails(getLogonDetailsCallback);
}]);