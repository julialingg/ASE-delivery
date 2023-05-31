import React, { useEffect, useState } from "react";
import {
  Button,
  Container,
  Dialog,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import Snackbar from "../../components/Snackbar";
import QrReader from "react-qr-reader";
import authService from "../../services/authService";
import deliveryService from "../../services/deliveryService";
export default function DelivererView() {
  const [scanning, setScanning] = useState(false);
  const [message, setMessage] = useState({});
  const [deliveries, setDeliveries] = useState([]);

  const stopScanning = () => {
    setScanning(false);
  };

  const handleScan = async (scanData) => {
    if (scanData && scanData !== "") {
      stopScanning();
      deliveryService.updateStatus(scanData).then((delivery) => {
        const index = deliveries.findIndex(({ id }) => id === delivery.id);
        const newValues = [...deliveries];
        newValues[index] = delivery;
        setDeliveries(newValues);
        setMessage({
          content: `Status of delivery ${scanData} has been updated`,
        });
      });
    }
  };

  const handleError = (err) => {
    console.error(err);
    setMessage({ content: err, severity: "error" });
  };
  const getDeliveries = () => {
    const user = authService.getUser();
    const delivererEmail = user.username;
    deliveryService
      .listDelivererDeliveries(delivererEmail)
      .then((deliveries) => setDeliveries(deliveries));
  };

  useEffect(() => {
    getDeliveries();
  }, []);

  return (
    <Container sx={{ textAlign: "center", pt: 2 }}>
      <Button variant="contained" onClick={() => setScanning(true)}>
        SCAN QR CODE
      </Button>
      <Dialog open={scanning} onClose={stopScanning}>
        <QrReader
          onError={handleError}
          onScan={handleScan}
          style={{ width: "300px" }}
        />
        <Button onClick={stopScanning}>Stop Scan</Button>
      </Dialog>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Delivery ID</TableCell>
              <TableCell>Box Name</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {deliveries.map(({ id, boxName, status }) => (
              <TableRow key={id}>
                <TableCell>{id}</TableCell>
                <TableCell>{boxName}</TableCell>
                <TableCell>{status}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      {message.content && (
        <Snackbar message={message} handleClose={() => setMessage({})} />
      )}
    </Container>
  );
}
