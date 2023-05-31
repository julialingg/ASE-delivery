const deliveryId = {
  key: "id",
  label: "Delivery ID",
  type: "text",
};

const boxName = {
  key: "boxName",
  label: "Target Box",
  type: "text",
};

const customerEmail = {
  key: "customerEmail",
  label: "Customer Email",
  type: "email",
};

const delivererEmail = {
  key: "delivererEmail",
  label: "Deliverer Email",
  type: "email",
};

const statuses = [
  {
    value: "CREATED",
    label: "Created",
  },
  {
    value: "COLLECTED",
    label: "Collected",
  },
  {
    value: "PLACED",
    label: "Placed",
  },
  {
    value: "DELIVERED",
    label: "Delivered",
  },
];

const status = {
  key: "status",
  label: "Status",
  type: "select",
  options: statuses,
};

export { deliveryId, boxName, customerEmail, delivererEmail, status };
