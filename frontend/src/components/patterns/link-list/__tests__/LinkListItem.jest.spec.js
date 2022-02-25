import { shallowMount } from '@vue/test-utils';
import VsLinkListItem from '../components/LinkListItem';

const slotContent = 'Link text';

const factoryShallowMount = () => shallowMount(VsLinkListItem, {
    slots: {
        default: slotContent,
    },
});

describe('VsLinkListItem', () => {
    describe(':slots', () => {
        it(':default - should render the slot content', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
