<template>
    <li
        class="vs-icon-list__item"
        data-test="vs-icon-list__item"
    >
        <VsIcon
            :name="icon"
            variant="dark"
            size="md"
            class="d-block mx-auto"
        />
        {{ label }}
    </li>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';

/**
 * An icon list item is a child component used within icon list components.
 * The component takes in an icon name and label that should be displayed under the icon as props.
 * They're displayed with an inline-table layout setting.
 *
 * @displayName Icon List Item
 */

export default {
    name: 'VsIconListItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
        /**
         * Label displayed under the icon
         */
        label: {
            type: String,
            required: true,
        },
        /**
         * The icon name will be passed to the vs-icon component to select which icon is rendered
         */
        icon: {
            type: String,
            required: true,
        },
    },
};
</script>

<style lang="scss">
.vs-icon-list__item {
    display: inline-table;
    text-align: center;
    width: 90px;
    margin-bottom: $spacer-4;
    padding: 0 $spacer-1;
    font-size: $h6-font-size;
    line-height: $line-height-standard;

    svg {
        margin-bottom: $spacer-3;
    }
}
</style>
