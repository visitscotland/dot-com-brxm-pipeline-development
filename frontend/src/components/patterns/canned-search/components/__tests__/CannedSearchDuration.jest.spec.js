import { mount } from '@vue/test-utils';
import VsCannedSearchDuration from '../CannedSearchDuration';

const durationIntro = 'Length';
const duration = '2 hours';

const factoryShallowMount = (propsData) => mount(VsCannedSearchDuration, {
    propsData: {
        durationIntro,
        duration,
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsCannedSearchDuration', () => {
    describe(':props', () => {
        it('should render the content of the `durationIntro` property', () => {
            expect(wrapper.html()).toContain(durationIntro);
        });

        it('should render the content of the `duration` property', () => {
            expect(wrapper.html()).toContain(duration);
        });
    });
});
