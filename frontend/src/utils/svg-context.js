import requireContext from 'require-context.macro';

let files = null;

// Jest doesn't handle the resourceQueried path properly through webpack and errors

if (typeof jest === 'undefined') {
    files = requireContext('../assets/svg?optimise', true, /^\.\/.*\.svg$/);
} else {
    files = requireContext('../assets/svg', true, /^\.\/.*\.svg$/);
}

const output = files;

export default output;
