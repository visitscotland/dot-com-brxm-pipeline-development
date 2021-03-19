import { shallowMount } from '@vue/test-utils';
import VsLinkList from '../LinkList';

const slotContent = 'Link text';

const factoryShallowMount = () => shallowMount(VsLinkList, {
    slots: {
        default: slotContent,
    },
});

describe('VsLinkList', () => {
    describe(':slots', () => {
        it(':default - should render the slot content', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
