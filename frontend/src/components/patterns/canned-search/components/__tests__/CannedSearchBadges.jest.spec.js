import { shallowMount } from '@vue/test-utils';
import VsCannedSearchBadges from '../CannedSearchBadges';

const badgeOne = 'B and B';
const badgeTwo = 'Offer';
const badgeThree = 'We are open';

const factoryShallowMount = () => shallowMount(VsCannedSearchBadges, {
    propsData: {
        badgeOne,
        badgeTwo,
        badgeThree,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsCannedSearchBadges', () => {
    describe(':props', () => {
        it('should render the content of the `badgeOne` property', () => {
            expect(wrapper.html()).toContain(badgeOne);
        });

        it('should render the content of the `badgeTwo` property', () => {
            expect(wrapper.html()).toContain(badgeTwo);
        });

        it('should render the content of the `badgeThree` property', () => {
            expect(wrapper.html()).toContain(badgeThree);
        });
    });
});
