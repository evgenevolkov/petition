'use strict';

describe('Subscription Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSubscription, MockUser, MockPetition;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSubscription = jasmine.createSpy('MockSubscription');
        MockUser = jasmine.createSpy('MockUser');
        MockPetition = jasmine.createSpy('MockPetition');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Subscription': MockSubscription,
            'User': MockUser,
            'Petition': MockPetition
        };
        createController = function() {
            $injector.get('$controller')("SubscriptionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'petitionApp:subscriptionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
