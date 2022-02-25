import { shallowMount } from '@vue/test-utils';
import VsMegalinks from '../Megalinks';

const factoryShallowMount = () => shallowMount(VsMegalinks, {
    propsData: {
        title: 'A megalinks title',
        buttonLink: 'http://www.visitscotland.com',
        variant: 'multi-image',
    },
    slots: {
        vsMegalinksIntro: '<p>Megalinks intro text</p>',
        vsMegalinksButton: 'Megalinks button text',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsMegalinks', () => {
    describe(':props', () => {
        it('should only render the button if an href is supplied', async() => {
            expect(wrapper.find('[data-test="vs-megalinks__button"]').exists()).toBe(true);

            await wrapper.setProps({
                buttonLink: '',
            });
            expect(wrapper.find('[data-test="vs-megalinks__button"]').exists()).toBe(false);
        });

        it('should render a variant class', () => {
            expect(wrapper.find('.vs-megalinks--multi-image').exists()).toBe(true);
        });

        it('should only show the intro if there is a heading', async() => {
            expect(wrapper.find('[data-test="vs-megalinks__intro"]').exists()).toBe(true);

            await wrapper.setProps({
                title: '',
            });
            expect(wrapper.find('[data-test="vs-megalinks__intro"]').exists()).toBe(false);
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a vsMegalinksHeading slot', () => {
            expect(wrapper.find('[data-test="vs-megalinks__heading"]').text()).toBe('A megalinks title');
        });

        it('renders content inserted in a vsMegalinksIntro slot', () => {
            expect(wrapper.find('[data-test="vs-megalinks__intro"]').html()).toContain('<p>Megalinks intro text</p>');
        });

        it('renders content inserted in a vsMegalinksButton slot', async() => {
            await wrapper.setProps({
                buttonLink: 'http://www.visitscotland.com',
            });

            expect(wrapper.find('[data-test="vs-megalinks__button"]').html()).toContain('Megalinks button text');
        });
    });
});
