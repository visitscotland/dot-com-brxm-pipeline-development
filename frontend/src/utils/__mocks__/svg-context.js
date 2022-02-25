const mockSvg = require('../../../test/unit/mocks/svgMock');

const mockFunc = jest.fn();

mockFunc.mockReturnValue(mockSvg);

module.exports = mockFunc;
