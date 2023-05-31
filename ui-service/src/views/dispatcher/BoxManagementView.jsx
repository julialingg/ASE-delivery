import React from "react";
import ManagementTable from "../../components/ManagementTable";
import { boxName, address, raspberryPiId } from "../../assets/boxAttrs";
import boxService from "../../services/boxService";

export default function DeliveryManagementView() {
  return (
    <ManagementTable
      name="Box"
      attrs={[boxName, address, raspberryPiId]}
      getData={boxService.list}
      creationProps={{
        attrs: [boxName, address, raspberryPiId],
        handleSubmit: boxService.create,
      }}
      updateProps={{
        attrs: [boxName, address, raspberryPiId],
        handleSubmit: boxService.update,
      }}
      deleteProps={{
        handleSubmit: boxService.remove,
      }}
    />
  );
}
