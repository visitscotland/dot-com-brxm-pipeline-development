import { shallowMount } from '@vue/test-utils';
import VsPsrModule from '../PsrModule';

const factoryShallowMount = (slotData) => shallowMount(VsPsrModule, {
    propsData: {
        configArr: [
            {
                subSearchType: 'acco',
            },
        ],
    },
    ...slotData,
});

describe('VsPsrModule', () => {
    describe(':slots', () => {
        it('should render heading slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsModuleHeading: 'This is the PSR module heading',
                },
            });

            expect(wrapper.html()).toContain('This is the PSR module heading');
        });

        it('should render intro slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsModuleIntro: 'This is the PSR module intro',
                },
            });

            expect(wrapper.html()).toContain('This is the PSR module intro');
        });
    });

    describe(':props', () => {
        it('should pass props to the child component', () => {
            const wrapper = factoryShallowMount();

            const embedStub = wrapper.find('vspsrembed-stub');
            expect(embedStub.attributes('config')).toBe('[object Object]');
        });
    });
});
