import React, { useState } from "react";
import { Tab, Tabs } from "@mui/material";
import DispatcherBoxManagementView from "./dispatcher/BoxManagementView";
import DispatcherDeliveryManagementView from "./dispatcher/DeliveryManagementView";
import DispatcherUserManagementView from "./dispatcher/UserManagementView";
import DelivererView from "./deliverer/DelivererView";
import CustomerActiveDeliveriesView from "./customer/CustomerActiveDeliveriesView";
import CustomerPastDeliveriesView from "./customer/CustomerPastDeliveriesView";

const userViews = {
  CUSTOMER: [
    {
      label: "Active Deliveries",
      element: <CustomerActiveDeliveriesView />
    },
    {
      label: "Past Deliveries",
      element: <CustomerPastDeliveriesView />
    },
  ],
  DELIVERER: [
    {
      element: <DelivererView />,
    },
  ],
  DISPATCHER: [
    {
      label: "Box Management",
      element: <DispatcherBoxManagementView />,
    },
    {
      label: "Delivery Management",
      element: <DispatcherDeliveryManagementView />,
    },
    {
      label: "User Management",
      element: <DispatcherUserManagementView />,
    },
  ],
};

export default function HomeView({ role }) {
  const [value, setValue] = useState(0);
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <React.Fragment>
      {userViews[role].length > 1 && (
        <Tabs value={value} onChange={handleChange} centered>
          {userViews[role].map(({ label }, index) => (
            <Tab key={index} value={index} label={label} />
          ))}
        </Tabs>
      )}
      {userViews[role][value].element}
    </React.Fragment>
  );
}
