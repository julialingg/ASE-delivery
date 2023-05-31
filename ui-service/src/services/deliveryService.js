import http from "./httpService";

const serviceURL = "http://localhost:10789/delivery";

const deliveryService = {
  list: () => http.get(`${serviceURL}`),
  listCustomerActiveDeliveries: (email) => http.get(`${serviceURL}/customer/${email}/active`),
  listCustomerPastDeliveries: (email) => http.get(`${serviceURL}/customer/${email}/past`),
  listDelivererDeliveries: (email) => http.get(`${serviceURL}/deliverer/${email}`),
  get: (id) => http.get(`${serviceURL}/${id}`),
  remove: (id) => http.remove(`${serviceURL}/${id}`),
  create: (data) => http.post(`${serviceURL}`, data),
  update: (data) => http.put(`${serviceURL}`, data),
  updateStatus: (id) => http.put(`${serviceURL}/status/${id}`),
};

export default deliveryService;
