import http from "./httpService";
const authServiceURL = "http://localhost:10789/ase-user";
const deliveryServiceURL = "http://localhost:10789/user";

const userService = {
  list: (role) => http.get(`${deliveryServiceURL}${role ? '?role=' + role : ''}`),
  remove: (id) =>
    Promise.all([
      http.remove(`${authServiceURL}/${id}`),
      http.remove(`${deliveryServiceURL}/${id}`),
    ]),
  create: ({ email, password, role, rfidToken }) =>
    http
      .post(`${authServiceURL}`, { username: email, role, password })
      .then((user) =>
        http.post(
          `${deliveryServiceURL}`,
          { id: user.id, email, role, rfidToken },
          (user) =>
            Promise.resolve(`User ${user.email} has been created successfully.`)
        )
      ),
  update: ({ id, email, role, rfidToken, password }) =>
    Promise.all([
      http.put(`${authServiceURL}`, {
        id,
        username: email,
        role,
        password: password ? password : "",
      }),
      http.put(`${deliveryServiceURL}`, { id, email, role, rfidToken }),
    ]).then((values) => values[1]),
};

export default userService;
