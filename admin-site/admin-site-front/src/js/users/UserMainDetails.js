import { Card, CardContent, Divider, Grid, Typography } from "@mui/material"

export const UserMainDetails = props => {
    console.log(props);
    const registrationDate = new Date(props.userDetails.registrationDate).toUTCString();
    return (
        <Card>
            <CardContent>
                <Typography variant="h4" color="text.secondary" gutterBottom>
                    Details
                </Typography>
                <Divider light />
                <Grid container spacing={2} paddingTop={5}>
                    <Grid item xs={6}>
                        <Typography variant="h6">
                            Email
                        </Typography>
                    </Grid>
                    <Grid item xs={6} data-test-id="email">
                        {props.userDetails.email}
                    </Grid>
                </Grid>

                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Typography variant="h6">
                            Registration Date
                        </Typography>
                    </Grid>
                    <Grid item xs={6} data-test-id="registration_date">
                        {registrationDate}
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    )
}