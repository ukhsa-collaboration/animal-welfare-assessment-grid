var manageAccordionControllers = angular.module('manageAccordionControllers', []);

manageAccordionControllers.controller('StudyStudyGroupAccordianCtrl', 
    function() {
    	this.accordionStatus = {
            isStudyGroupAnimalsOpen: true,
            isStudyStudyGroupsOpen: false,
            isUploadStudyGroupAnimalsUploadOpen : false,	//	TODO
            isUploadStudyStudyGroupOpen : false
        };
    });