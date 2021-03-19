import { shallowMount } from '@vue/test-utils';
import VsStretchedLinkPanel from '../components/StretchedLinkPanel';

describe('VsStretchedLinkCard', () => {
    describe(':props', () => {
        it('should render a `.stretched-link-panel--days` class if the days prop is defined', () => {
            const wrapper = shallowMount(VsStretchedLinkPanel, {
                propsData: {
                    days: '10',
                },
            });
            expect(wrapper.find('[data-test="stretched-link-panel--days"]').exists()).toBe(true);
        });

        it('should render an element with the text prop content if it is defined', () => {
            const wrapper = shallowMount(VsStretchedLinkPanel, {
                propsData: {
                    text: 'Panel text',
                },
            });
            expect(wrapper.find('[data-test="stretched-link-panel__icon"]').text()).toBe('Panel text');
            expect(wrapper.find('[data-test="stretched-link-panel--days"]').exists()).toBe(false);
        });
    });
});
