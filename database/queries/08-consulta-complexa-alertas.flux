from(bucket: "monitoramento")
  |> range(start: 2024-06-17T00:00:00Z, stop: 2024-06-18T00:00:00Z)
  |> filter(fn: (r) => r._measurement == "sensor_reading")
  |> filter(fn: (r) => r._field == "temperature" or r._field == "co2")
  |> pivot(rowKey: ["_time", "sensorId", "room", "status"], columnKey: ["_field"], valueColumn: "_value")
  |> map(fn: (r) => ({
      r with
      alert_level:
        if r.temperature >= 30.0 or r.co2 >= 1200.0 then "CRITICO"
        else if r.temperature >= 28.0 or r.co2 >= 1000.0 then "ATENCAO"
        else "NORMAL"
  }))