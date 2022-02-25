import { mount } from '@vue/test-utils';
import VsTag from '../Tag';

const slotContent = 'Slot Content';

const factoryMount = () => mount(VsTag, {
    propsData: {
        href: 'https://www.visitscotland.com/',
    },
    slots: {
        default: slotContent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryMount();
});

describe('VsTag', () => {
    it('should render a component with the data-test attribute `.vs-tag`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-tag');
    });

    describe(':props', () => {
        it('should render url `https://www.visitscotland.com/` when passed as `href` prop', () => {
            expect(wrapper.find('.vs-link').attributes().href).toBe('https://www.visitscotland.com/');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
