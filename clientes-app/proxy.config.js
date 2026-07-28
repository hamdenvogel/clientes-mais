const proxy = [
  {
    context: '/oauth',
    target: 'http://localhost:8080',
    pathRewrite: {'^/oauth' : ''}
  }
];
module.exports = proxy;