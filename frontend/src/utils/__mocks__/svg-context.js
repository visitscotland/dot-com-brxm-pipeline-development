const mockSvg = require('../../../test/unit/mocks/svgMock.js');

const mockFunc = jest.fn();

mockFunc.mockReturnValue(mockSvg);

module.exports = mockFunc;
