'use strict';

describe('Petition Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPetition, MockUser, MockSubscription;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPetition = jasmine.createSpy('MockPetition');
        MockUser = jasmine.createSpy('MockUser');
        MockSubscription = jasmine.createSpy('MockSubscription');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Petition': MockPetition,
            'User': MockUser,
            'Subscription': MockSubscription
        };
        createController = function() {
            $injector.get('$controller')("PetitionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'petitionApp:petitionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
