import http from "./httpService";

const serviceURL = "http://localhost:10789/box";

const boxService = {
  list: () => http.get(`${serviceURL}`),
  remove: (id) => http.remove(`${serviceURL}/${id}`),
  create: (data) => http.post(`${serviceURL}`, data),
  update: (data) => http.put(`${serviceURL}`, data),
};

export default boxService;
