leituras =
  from(bucket: "monitoramento")
    |> range(start: 2024-06-17T00:00:00Z, stop: 2024-06-18T00:00:00Z)
    |> filter(fn: (r) => r._measurement == "sensor_reading")
    |> filter(fn: (r) => r._field == "temperature")
    |> keep(columns: ["_time", "sensorId", "room", "_value"])
    |> rename(columns: {_value: "temperature"})
    |> group(columns: ["sensorId"])

sensores =
  from(bucket: "monitoramento")
    |> range(start: 2024-06-17T00:00:00Z, stop: 2024-06-18T00:00:00Z)
    |> filter(fn: (r) => r._measurement == "sensor_info")
    |> filter(fn: (r) => r._field == "threshold_temp")
    |> keep(columns: ["sensorId", "model", "_value"])
    |> rename(columns: {_value: "threshold_temp"})
    |> group(columns: ["sensorId"])

join(tables: {leituras: leituras, sensores: sensores}, on: ["sensorId"])