import { Button, Card, CardActions, CardContent, Divider, Grid, Typography } from "@mui/material"

export const UserPreferences = props => {
    const {daysToSendOn, sites, headlineCount} = props.preferences;
    return (
        <Card>
            <CardContent>
                <Typography variant="h4" color="text.secondary" gutterBottom>
                    Preferences
                </Typography>
                <Divider light />
                <Grid container spacing={2} paddingTop={5}>
                    <Grid item xs={6}>
                        <Typography variant="h6">
                            Sites
                        </Typography>
                    </Grid>
                    <Grid item xs={6} data-test-id="sites">
                        {sites.join(', ')}
                    </Grid>
                </Grid>

                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Typography variant="h6">
                            Days to send on
                        </Typography>
                    </Grid>
                    <Grid item xs={6} data-test-id="days_to_send_on">
                        {daysToSendOn.join(', ')}
                    </Grid>
                </Grid>

                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Typography variant="h6">
                            Headline count
                        </Typography>
                    </Grid>
                    <Grid item xs={6} data-test-id="headline_count">
                        {headlineCount}
                    </Grid>
                </Grid>
            </CardContent>
            <Divider light />
            <CardActions>
                <Button size="medium">Edit</Button>
            </CardActions>
        </Card>
    )
}