<template>
    <VsRow>
        <VsCol
            cols="12"
            sm="6"
            md="4"
            xl="3"
            v-for="path in icons"
            :key="path"
        >
            <div class="card d-flex align-items-center">
                <VsIcon
                    :name="path"
                    size="xl"
                    class="my-4"
                />
                <pre>{{ path }}</pre>
            </div>
        </VsCol>
    </VsRow>
</template>

<script>
import VsIcon from '@components/elements/icon/';
import { VsRow, VsCol } from '@components/elements/grid';
import { trimStart, map } from 'lodash';

const getAllIcons = () => {
    const all = require.context('#assets/svg/icons', false, /^\.\/.*\.svg$/);
    return map(all.keys(), (key) => trimStart(key, './').replace('.svg', ''));
};

const allIcons = getAllIcons();

export default {
    name: 'Icons',
    components: {
        VsIcon,
        VsCol,
        VsRow,
    },
    data() {
        return {
            icons: allIcons,
        };
    },
    methods: {
    },
};

</script>

<style lang="scss" scoped>
.card {
    padding: 0.5rem 0;
    margin-bottom: 1rem;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);

    pre {
        white-space: normal;
        text-align: center;
    }
}
</style>

<docs>
  ```jsx
    <icons />
  ```
</docs>
