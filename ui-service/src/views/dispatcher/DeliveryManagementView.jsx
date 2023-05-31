import React, { useEffect, useRef, useState } from "react";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  Typography,
} from "@mui/material";
import ManagementTable from "../../components/ManagementTable";
import Autocomplete from "../../components/form/Autocomplete";
import {
  deliveryId,
  boxName,
  customerEmail,
  delivererEmail,
  status,
} from "../../assets/deliveryAttrs";
import deliveryService from "../../services/deliveryService";
import userService from "../../services/userService";
import boxService from "../../services/boxService";
import QRCode from "qrcode";

export default function DeliveryManagementView() {
  const tableRef = useRef(null);
  const [currentDelivery, setCurrentDelivery] = useState({});
  const [qrCode, setQRCode] = useState("");
  const [openDialog, setOpenDialog] = useState(false);

  useEffect(() => {
    if (currentDelivery.id) {
      QRCode.toDataURL(currentDelivery.id, (err, string) => {
        setQRCode(err ? "" : string);
      });
    }
  }, [currentDelivery]);

  const updateCurrentDelivery = (delivery) => {
    setCurrentDelivery(delivery);
    setOpenDialog(true);
  };

  const onSaveQRCode = () => {
    const link = document.createElement("a");
    link.download = `${currentDelivery.id}.png`;
    link.href = qrCode;
    link.click();
  };

  const onCreate = (data) =>
    deliveryService.create(data).then((delivery) => {
      updateCurrentDelivery(delivery);
      return Promise.resolve(delivery);
    });

  const Delivery = () => {
    const currentValues = tableRef.current
      ? tableRef.current.selectedRow.data
      : {};
    const currentBoxName = currentValues[boxName.key];
    const currentCustomerEmail = currentValues[customerEmail.key];
    const currentDelivererEmail = currentValues[delivererEmail.key];

    const [currentBox, setCurrentBox] = useState(
      currentBoxName ? {boxName:currentBoxName, customerEmail: currentCustomerEmail} : null
    );

    const [currentCustomer, setCurrentCustomer] = useState(
      currentCustomerEmail ? { email: currentCustomerEmail } : null
    );

    const [currentDeliverer, setCurrentDeliverer] = useState(
      currentDelivererEmail ? { email: currentDelivererEmail } : null
    );

    const onBoxChange = (box) => {

      if (box && box.customerEmail) {
        setCurrentCustomer({ email: box.customerEmail});
      }
      setCurrentBox(box);
    };

    return (
      <>
        <Autocomplete
          label={boxName.label}
          name={boxName.key}
          labelKey="boxName"
          getOptions={boxService.list}
          defaultValue={currentBox}
          onChange={onBoxChange}
        />
        <Autocomplete
          label={customerEmail.label}
          name={customerEmail.key}
          labelKey="email"
          getOptions={
            currentBox && currentBox.customerEmail
              ? () => [currentCustomer]
              : () => userService.list('CUSTOMER')
          }
          defaultValue={currentCustomer}
        />
        <Autocomplete
          label={delivererEmail.label}
          name={delivererEmail.key}
          labelKey="email"
          getOptions={()=> userService.list('DELIVERER')}
          defaultValue={currentDeliverer}
          onChange={setCurrentDeliverer}
        />
      </>
    );
  };

  return (
    <>
      <ManagementTable
        ref={tableRef}
        name="Delivery"
        attrs={[deliveryId, boxName, customerEmail, delivererEmail, status]}
        getData={deliveryService.list}
        creationProps={{
          attrs: [],
          handleSubmit: onCreate,
        }}
        updateProps={{
          attrs: [{ ...deliveryId, readOnly: true }, status],
          handleSubmit: deliveryService.update,
        }}
        formMixin={<Delivery />}
        deleteProps={{
          handleSubmit: deliveryService.remove,
        }}
        selectableRow="true"
        onRowSelect={updateCurrentDelivery}
      />
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>Delivery Info</DialogTitle>
        <DialogContent sx={{ display: "grid" }}>
          {[deliveryId, boxName, customerEmail, delivererEmail, status].map(
            ({ label, key }) => (
              <Typography key={key} variant="body1" component="p">
                {label + ": " + currentDelivery[key]}
              </Typography>
            )
          )}
          <img alt="qrcode" src={qrCode} style={{ justifySelf: "center" }} />
          <Button onClick={onSaveQRCode}>Save QR Code</Button>
        </DialogContent>
      </Dialog>
    </>
  );
}
