function hashCode() {
    const r = Math.random()
        .toString(36)
        .substring(7);
    return r;
}

export default hashCode;
