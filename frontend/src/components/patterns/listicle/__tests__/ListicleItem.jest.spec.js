import { shallowMount } from '@vue/test-utils';

import VsListicleItem from '../ListicleItem';

const hippoDetailsSlot = 'Hippo details slot';
const imageSlot = 'Image slot';
const descriptionSlot = 'Description slot';
const facilitiesSlot = 'Facilities slot';

const factoryShallowMount = (propsData) => shallowMount(VsListicleItem, {
    propsData: {
        ...propsData,
    },
    slots: {
        'hippo-details': hippoDetailsSlot,
        'image-slot': imageSlot,
        'description-slot': descriptionSlot,
        'facilities-slot': facilitiesSlot,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsListicleItem', () => {
    it('should render a component with the data-test attribute `vs-listicle-item`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-listicle-item');
    });

    describe(':slots', () => {
        it('renders content inserted into the `hippo-details` slot', () => {
            expect(wrapper.text()).toContain(hippoDetailsSlot);
        });

        it('renders content inserted into the `image-slot` slot', () => {
            expect(wrapper.text()).toContain(imageSlot);
        });

        it('renders content inserted into the `description-slot` slot', () => {
            expect(wrapper.text()).toContain(descriptionSlot);
        });

        it('renders content inserted into the `facilities-slot` slot', () => {
            expect(wrapper.text()).toContain(facilitiesSlot);
        });
    });
});
