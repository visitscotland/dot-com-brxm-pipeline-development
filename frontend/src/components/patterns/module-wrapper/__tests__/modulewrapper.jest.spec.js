import { shallowMount } from '@vue/test-utils';
import VsModuleWrapper from '../ModuleWrapper';

describe('VsModuleWrapper', () => {
    describe(':slots', () => {
        it('renders content inserted in the default slot', () => {
            const wrapper = shallowMount(VsModuleWrapper, {
                slots: {
                    default: 'Default slot content',
                },
            });
            expect(wrapper.text()).toContain('Default slot content');
        });

        it('renders content inserted in a vsModuleWrapperHeading slot', () => {
            const wrapper = shallowMount(VsModuleWrapper, {
                slots: {
                    vsModuleWrapperHeading: 'Module wrapper heading',
                },
            });
            expect(wrapper.find('[data-test="vs-module-wrapper__heading"]').text()).toBe('Module wrapper heading');
        });

        it('renders content inserted in a vsModuleWrapperIntro slot', () => {
            const wrapper = shallowMount(VsModuleWrapper, {
                slots: {
                    vsModuleWrapperIntro: '<p>Module wrapper content</p>',
                },
            });
            expect(wrapper.find('[data-test="vs-module-wrapper__intro"]').html()).toContain('<p>Module wrapper content</p>');
        });

        it('does not render the heading if no vsModuleWrapperHeading slot is supplied', () => {
            const wrapper = shallowMount(VsModuleWrapper, {
                slots: {
                    vsModuleWrapperHeading: '',
                },
            });
            expect(wrapper.find('[data-test="vs-module-wrapper__heading"]').exists()).toBe(false);
        });

        it('does not render the intro if no vsModuleWrapperHeading slot is supplied', () => {
            const wrapper = shallowMount(VsModuleWrapper, {
                slots: {
                    vsModuleWrapperIntro: '',
                },
            });
            expect(wrapper.find('[data-test="vs-module-wrapper__intro"]').exists()).toBe(false);
        });
    });
});
