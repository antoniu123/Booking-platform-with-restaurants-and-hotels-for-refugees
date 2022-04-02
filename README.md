# Auth0 Implementation with Spring Boot and React.


Configure application.properties in Spring Boot Application

```
auth0.audience=
spring.security.oauth2.resourceserver.jwt.issuer-uri=
```


Configure config.json in React App

```

{
    "domain": "",
    "clientId": "",
    "audience": "",
    "scope": "read:current_user"
}
```

Know how these values can be obtained in Medium Story:

./gradle build
./gradle bootRun
```

To run React App:
```
npm i
npm start (This is for Development)
```
ON Auth Pipelines -> Rules new Rule need to be created
/*
function (user, context, callback) {
  const namespace = 'http://schemas.microsoft.com/ws/2008/06/identity/claims';
  const assignedRoles = (context.authorization || {}).roles;

  let idTokenClaims = context.idToken || {};
  let accessTokenClaims = context.accessToken || {};

  idTokenClaims[`${namespace}/role`] = assignedRoles;
  accessTokenClaims[`${namespace}/role`] = assignedRoles;

  context.idToken = idTokenClaims;
  context.accessToken = accessTokenClaims;

  callback(null, user, context);
}
*/