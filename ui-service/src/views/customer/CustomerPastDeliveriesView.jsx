import React, { useEffect, useState } from "react";
import {
  Box,
  Card,
  CardContent,
  Container,
  Divider,
  Typography,
  Stack,
} from "@mui/material";
import authService from "../../services/authService";
import deliveryService from "../../services/deliveryService";

export default function CustomerPastDeliveriesView() {
  const [deliveries, setDeliveries] = useState([]);


  useEffect(() => {
    const user = authService.getUser();
    const email = user.username;
    deliveryService.listCustomerPastDeliveries(email).then((deliveries) => setDeliveries(deliveries));
  }, []);

  return (
    <Container
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        pt: 2,
      }}
    >
      <Stack spacing={5} sx={{ width: "70%" }}>
        {deliveries.map(({ id, boxName, status }) => (
          <Card sx={{ minWidth: 275 }} key={id}>
            <CardContent sx={{ px: 3 }}>
              <Typography variant="subtitle1" gutterBottom component="div">
                Delivery ID: {id}
              </Typography>
              <Divider sx={{ mb: 2 }} />
              <Box>
                  <Typography variant="body1" gutterBottom>
                    Box Name: {boxName}
                  </Typography>
              </Box>
            </CardContent>
          </Card>
        ))}
      </Stack>
    </Container>
  );
}
